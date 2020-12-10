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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;


/**
 * Unit test for Function class.
 */
class FunctionTest {
  /**
   * Unit test for HttpTriggerJava method.
   */
  @Test
  void testHttpTriggerJava() throws Exception {
    // Setup
    @SuppressWarnings("unchecked")
    final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

    final Map<String, String> queryParams = new HashMap<>();
    queryParams.put("name", "Azure");
    doReturn(queryParams).when(req).getQueryParameters();

    final Optional<String> queryBody = Optional.empty();
    doReturn(queryBody).when(req).getBody();

    doAnswer(invocation -> {
      final HttpStatus status = (HttpStatus) invocation.getArguments()[0];
      return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
    }).when(req).createResponseBuilder(any(HttpStatus.class));

    final ExecutionContext context = mock(ExecutionContext.class);
    doReturn(Logger.getGlobal()).when(context).getLogger();

    // Invoke
    final HttpResponseMessage ret = new Function().events(req, context);

    // Verify
    assertEquals(ret.getStatus(), HttpStatus.OK);
  }
}
