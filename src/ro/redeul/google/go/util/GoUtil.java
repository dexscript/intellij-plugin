package ro.redeul.google.go.util;

import com.intellij.ide.Bootstrap;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import ro.redeul.google.go.lang.psi.GoFile;
import ro.redeul.google.go.lang.psi.GoPsiElement;
import ro.redeul.google.go.lang.psi.expressions.GoExpr;
import ro.redeul.google.go.lang.psi.expressions.binary.GoAdditiveExpression;
import ro.redeul.google.go.lang.psi.expressions.binary.GoMultiplicativeExpression;
import ro.redeul.google.go.lang.psi.expressions.binary.GoRelationalExpression;
import ro.redeul.google.go.lang.psi.expressions.literals.GoLiteral;
import ro.redeul.google.go.lang.psi.expressions.literals.GoLiteralFunction;
import ro.redeul.google.go.lang.psi.expressions.literals.GoLiteralIdentifier;
import ro.redeul.google.go.lang.psi.expressions.literals.composite.GoLiteralComposite;
import ro.redeul.google.go.lang.psi.expressions.primary.GoCallOrConvExpression;
import ro.redeul.google.go.lang.psi.expressions.primary.GoLiteralExpression;
import ro.redeul.google.go.lang.psi.toplevel.GoFunctionDeclaration;
import ro.redeul.google.go.lang.psi.toplevel.GoFunctionParameter;
import ro.redeul.google.go.lang.psi.toplevel.GoFunctionParameterList;
import ro.redeul.google.go.lang.psi.types.GoPsiType;
import ro.redeul.google.go.lang.psi.types.GoPsiTypeFunction;
import ro.redeul.google.go.lang.psi.types.GoPsiTypePointer;
import ro.redeul.google.go.lang.psi.typing.*;
import ro.redeul.google.go.lang.psi.utils.GoExpressionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ro.redeul.google.go.lang.psi.utils.GoPsiUtils.*;

/**
 * Author: Toader Mihai Claudiu <mtoader@gmail.com>
 * <p/>
 * Date: Aug 19, 2010
 * Time: 4:18:54 AM
 */
public class GoUtil {

    /**
     * Gets the idea home directory.
     * Note: Copied over from IDEA Main class but the home directory doesn't seem to be properly made available to
     * plugins.
     *
     * @return The idea home directory
     */
    private static File getIdeaHomeDir() {
        URL url = Bootstrap.class.getResource("");
        if (url == null || !"jar".equals(url.getProtocol())) return null;

        String path = url.getPath();

        int start = path.indexOf("file:/");
        int end = path.indexOf("!/");
        if (start == -1 || end == -1) return null;

        String jarFileUrl = path.substring(start, end);

        try {
            File bootstrapJar = new File(new URI(jarFileUrl));
            return bootstrapJar.getParentFile().getParentFile();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static boolean testGoHomeFolder(String goRoot) {
        return goRoot != null
                && goRoot.trim().length() > 0 && new File(goRoot).isDirectory();
    }

    private final static Pattern RE_PACKAGE_TARGET =
            Pattern.compile("^TARG=([^\\s]+)\\s*$", Pattern.MULTILINE);

    /**
     * Returns a string if there is a TARG=xxx specified in the provided makefile and null if there is no such file.
     *
     * @param makefile the file we want to test (can be null)
     * @return the specified target or null
     */
    public static String getTargetFromMakefile(VirtualFile makefile) {
        if (makefile == null) {
            return null;
        }

        try {
            String content = new String(makefile.contentsToByteArray(), "UTF-8");

            Matcher matcher = RE_PACKAGE_TARGET.matcher(content);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (IOException e) {
            //
        }

        return null;
    }


    public static String getFuncDecAsParam(GoFunctionDeclaration goLiteralFunction) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("func(");

        int counter = 0;
        for (GoFunctionParameter parameter :
                goLiteralFunction.getParameters()) {
            if (counter != 0)
                stringBuilder.append(',');
            stringBuilder.append(parameter.getType().getText());
            counter++;
        }
        stringBuilder.append(')');
        counter = 0;
        GoFunctionParameter[] results = goLiteralFunction.getResults();

        if (results.length > 1)
            stringBuilder.append('(');

        for (GoFunctionParameter parameter : results) {
            if (counter != 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(parameter.getType().getText());
            counter++;
        }

        if (counter > 1)
            stringBuilder.append(')');

        return stringBuilder.toString();
    }

    public static boolean isFunctionNameIdentifier(PsiElement e) {
        if (!psiIsA(e, GoLiteralExpression.class))
            return false;

        GoLiteral literal = ((GoLiteralExpression) e).getLiteral();
        if (!(literal instanceof GoLiteralIdentifier))
            return false;

        if (((GoLiteralIdentifier) literal).isQualified())
            return false;

        if (!psiIsA(e.getParent(), GoCallOrConvExpression.class))
            return false;

        // function name is the first element of its parent.
        return e.getStartOffsetInParent() == 0;
    }

    /**
     * Helper method to generate function arguments type, based on param being passed to the function
     *
     * @param e -> parent -> GoCallOrConvExpression
     * @return the generated arugment list ex: arg0 int, arg1 string
     */
    public static String InspectionGenFuncArgs(PsiElement e) {
        StringBuilder stringBuilder = new StringBuilder();
        int arg = 0;
        if (isFunctionNameIdentifier(e)) {
            for (GoExpr argument : ((GoCallOrConvExpression) e.getParent()).getArguments()) {
                if (arg != 0)
                    stringBuilder.append(',');
                stringBuilder.append("arg").append(arg).append(" ");
                PsiElement firstChildExp = argument.getFirstChild();

                if (argument instanceof GoCallOrConvExpression ||
                        argument instanceof GoMultiplicativeExpression ||
                        argument instanceof GoAdditiveExpression ||
                        //  argument instanceof GoLiteralExpression ||
                        firstChildExp instanceof GoLiteralIdentifier ||
                        firstChildExp instanceof GoLiteralComposite ||
                        firstChildExp.getText().equals("&")) {

                    GoType[] goTypes = argument.getType();
                    if (goTypes.length > 0) {
                        GoType goType = goTypes[0];
                        if (goType instanceof GoTypeSlice) {
                            stringBuilder.append(((GoTypeSlice) goType).getPsiType().getText());
                        } else if (goType instanceof GoTypeName) {
                            if (firstChildExp.getText().equals("&"))
                                stringBuilder.append('*');
                            stringBuilder.append(((GoTypeName) goType).getPsiType().getText());

                        } else if (goType instanceof GoTypePointer) {
                            goType = ((GoTypePointer) goType).getTargetType();
                            stringBuilder.append('*');
                            stringBuilder.append(((GoTypeName) goType).getPsiType().getText());
                        } else if (goType instanceof GoTypeFunction) {
                            stringBuilder.append(getFuncDecAsParam(((GoFunctionDeclaration) ((GoTypeFunction) goType).getPsiType())));
                        } else {

                            //Get the expression element
                            final PsiReference[] references = firstChildExp.getReferences();
                            if (references.length > 0) {
                                GoPsiElement resl_element = (GoPsiElement) references[0].resolve().getParent().getLastChild();
                                GoLiteralFunction fn = (GoLiteralFunction) resl_element.getFirstChild();
                                stringBuilder.append(getFuncDecAsParam(fn));
                            } else {
                                stringBuilder.append("interface{}");
                            }

                        }
                    } else {
                        PsiElement firstChild = firstChildExp.getFirstChild();
                        if (firstChild instanceof GoLiteralFunction) {
                            GoPsiType[] returnType = ((GoLiteralFunction) firstChild).getReturnType();
                            if (returnType.length > 0) {
                                stringBuilder.append(returnType[0].getText());
                            } else {
                                stringBuilder.append("interface{}");
                            }
                        } else {
                            stringBuilder.append("interface{}");
                        }
                    }

                } else if (firstChildExp instanceof GoLiteralFunction) {
                    stringBuilder.append(GoUtil.getFuncDecAsParam((GoLiteralFunction) firstChildExp));
                } else if (argument instanceof GoRelationalExpression) {
                    stringBuilder.append("bool");
                } else {
                    stringBuilder.append(((GoLiteral) firstChildExp).getType().name().toLowerCase());
                }
                arg++;
            }
        } else {
            GoCallOrConvExpression parentOfTypeCallOrConvExpression = findParentOfType(e, GoCallOrConvExpression.class);
            GoFunctionDeclaration goFunctionDeclaration = GoExpressionUtils.resolveToFunctionDeclaration(parentOfTypeCallOrConvExpression);

            if (goFunctionDeclaration != null) {

                int myIndex = 0;

                for (GoExpr argum : parentOfTypeCallOrConvExpression.getArguments()) {
                    if (argum.equals(e) || argum.getFirstChild().equals(e))
                        break;
                    myIndex++;
                }

                GoFunctionParameter[] parameter = goFunctionDeclaration.getParameters();

                if (myIndex < parameter.length) {
                    GoPsiType type = parameter[myIndex].getType();
                    if (type instanceof GoPsiTypeFunction) {
                        GoFunctionParameterList goFunctionParameterList = findChildOfClass(type, GoFunctionParameterList.class);
                        final String packageName = ((GoFile) e.getContainingFile()).getPackageName();
                        if (goFunctionParameterList != null) {
                            for (GoFunctionParameter parameter1 : goFunctionParameterList.getFunctionParameters()) {
                                if (arg > 0)
                                    stringBuilder.append(',');

                                stringBuilder.append("arg").append(arg).append(' ');

                                final GoPsiType type1 = parameter1.getType();
                                if (type1.getPackageName().equals(packageName)) {
                                    stringBuilder.append(type1.getText());
                                } else if (type1 instanceof GoPsiTypePointer) {
                                    stringBuilder.append('*').append(((GoPsiType) ((GoPsiTypePointer) type1).getTargetType()).getQualifiedName());
                                } else {
                                    stringBuilder.append(type1.getQualifiedName());
                                }
                                arg++;
                            }
                        }

                    }
                }

            } else {
                return "";
            }

        }

        return stringBuilder.toString();
    }
}

