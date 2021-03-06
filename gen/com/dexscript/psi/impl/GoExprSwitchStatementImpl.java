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

import com.dexscript.psi.*;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.dexscript.psi.GoPsiTreeUtil;
import static com.dexscript.parser.GoTypes.*;

public class GoExprSwitchStatementImpl extends GoSwitchStatementImpl implements GoExprSwitchStatement {

  public GoExprSwitchStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GoVisitor visitor) {
    visitor.visitExprSwitchStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GoVisitor) accept((GoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GoExprCaseClause> getExprCaseClauseList() {
    return GoPsiTreeUtil.getChildrenOfTypeAsList(this, GoExprCaseClause.class);
  }

  @Override
  @Nullable
  public GoExpression getExpression() {
    return GoPsiTreeUtil.getChildOfType(this, GoExpression.class);
  }

  @Override
  @Nullable
  public GoStatement getStatement() {
    return GoPsiTreeUtil.getChildOfType(this, GoStatement.class);
  }

  @Override
  @NotNull
  public GoSwitchStart getSwitchStart() {
    return notNullChild(GoPsiTreeUtil.getChildOfType(this, GoSwitchStart.class));
  }

  @Override
  @NotNull
  public PsiElement getLbrace() {
    return notNullChild(findChildByType(LBRACE));
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

  @Override
  @Nullable
  public PsiElement getSemicolon() {
    return findChildByType(SEMICOLON);
  }

}
