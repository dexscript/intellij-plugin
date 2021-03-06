/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
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

package com.dexscript.stubs.types;

import com.dexscript.psi.GoFieldDefinition;
import com.dexscript.psi.impl.GoFieldDefinitionImpl;
import com.dexscript.stubs.GoFieldDefinitionStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GoFieldDefinitionStubElementType extends GoNamedStubElementType<GoFieldDefinitionStub, GoFieldDefinition> {
  public GoFieldDefinitionStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public GoFieldDefinition createPsi(@NotNull GoFieldDefinitionStub stub) {
    return new GoFieldDefinitionImpl(stub, this);
  }

  @NotNull
  @Override
  public GoFieldDefinitionStub createStub(@NotNull GoFieldDefinition psi, StubElement parentStub) {
    return new GoFieldDefinitionStub(parentStub, this, psi.getName(), psi.isPublic());
  }

  @Override
  public void serialize(@NotNull GoFieldDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isPublic());
  }

  @NotNull
  @Override
  public GoFieldDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new GoFieldDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean());
  }
}
