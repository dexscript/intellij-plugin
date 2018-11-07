/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This is a generated file. Not intended for manual editing.
package com.dexscript.psi.impl;

import java.util.List;

import com.dexscript.psi.GoImportDeclaration;
import com.dexscript.psi.GoImportSpec;
import com.dexscript.psi.GoPsiTreeUtil;
import com.dexscript.psi.GoVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import static com.dexscript.parser.GoTypes.*;

public class GoImportDeclarationImpl extends GoCompositeElementImpl implements GoImportDeclaration {

  public GoImportDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GoVisitor visitor) {
    visitor.visitImportDeclaration(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GoVisitor) accept((GoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GoImportSpec> getImportSpecList() {
    return GoPsiTreeUtil.getChildrenOfTypeAsList(this, GoImportSpec.class);
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

  @Override
  @NotNull
  public PsiElement getImport() {
    return notNullChild(findChildByType(IMPORT));
  }

  @NotNull
  public GoImportSpec addImportSpec(String packagePath, String alias) {
    return GoPsiImplUtil.addImportSpec(this, packagePath, alias);
  }

}
