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

import com.dexscript.psi.GoPackageClause;
import com.dexscript.psi.GoVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import static com.dexscript.parser.GoTypes.*;
import com.goide.stubs.GoPackageClauseStub;
import com.intellij.psi.stubs.IStubElementType;

public class GoPackageClauseImpl extends GoStubbedElementImpl<GoPackageClauseStub> implements GoPackageClause {

  public GoPackageClauseImpl(GoPackageClauseStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public GoPackageClauseImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GoVisitor visitor) {
    visitor.visitPackageClause(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GoVisitor) accept((GoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  @Override
  @NotNull
  public PsiElement getPackage() {
    return notNullChild(findChildByType(PACKAGE));
  }

  @Nullable
  public String getName() {
    return GoPsiImplUtil.getName(this);
  }

}
