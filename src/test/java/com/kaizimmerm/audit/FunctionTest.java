/*
 * Copyright © 2020 Kai Zimmermann (kaizimmerm.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaizimmerm.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


/**
 * Unit test for Function class.
 */
class FunctionTest {
  @SuppressWarnings("unchecked")
  @Test
  void testHttpTriggerJava() throws Exception {
    // Setup

    final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

    final String testBody = "teststring";

    final Optional<String> queryBody = Optional.of(testBody);
    doReturn(queryBody).when(req).getBody();

    doAnswer(invocation -> {
      final HttpStatus status = (HttpStatus) invocation.getArguments()[0];
      return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
    }).when(req).createResponseBuilder(any(HttpStatus.class));

    final ExecutionContext context = mock(ExecutionContext.class);
    doReturn(Logger.getGlobal()).when(context).getLogger();

    final ArgumentCaptor<OutputBinding<String>> endpointCreateCaptor =
        ArgumentCaptor.forClass(OutputBinding.class);

    final OutputBinding<String> output = mock(OutputBinding.class);

    // Invoke
    final HttpResponseMessage ret = new Function().storepullrequest(req, context, output);

    // Verify
    verify(output).setValue(eq(testBody));
    assertThat(ret.getStatus()).isEqualTo(HttpStatus.OK);
  }
}
