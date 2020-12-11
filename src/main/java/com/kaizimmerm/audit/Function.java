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

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

  @FunctionName("storepullrequest")
  public HttpResponseMessage storepullrequest(@HttpTrigger(name = "request",
      methods = {HttpMethod.POST},
      route = "pullrequests",
      authLevel = AuthorizationLevel.FUNCTION) final HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context,
      @CosmosDBOutput(name = "database", databaseName = "devops", collectionName = "pullrequests",
          connectionStringSetting = "AzureCosmosDBConnection") final OutputBinding<String> document) {

    return processStoreRequest(request, context, document);
  }

  @FunctionName("getpullrequests")
  public HttpResponseMessage pullrequests(@HttpTrigger(name = "req", methods = {HttpMethod.GET},
    route = "pullrequests/{pullRequestId}",
      authLevel = AuthorizationLevel.FUNCTION) final HttpRequestMessage<Optional<String>> request,
      @CosmosDBInput(name = "database", databaseName = "devops", collectionName = "pullrequests",
          partitionKey = "{pullRequestId}",
          connectionStringSetting = "AzureCosmosDBConnection") final String[] items) {

    if (items == null) {
      request.createResponseBuilder(HttpStatus.NOT_FOUND).build();
    }

    return request.createResponseBuilder(HttpStatus.OK)
                          .header("Content-Type", "application/json")
                          .body(items)
                          .build();
  }

  @FunctionName("storeworkitem")
  public HttpResponseMessage storeworkitem(@HttpTrigger(name = "request",
      methods = {HttpMethod.POST},
      route = "workitems",
      authLevel = AuthorizationLevel.FUNCTION) final HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context,
      @CosmosDBOutput(name = "database", databaseName = "devops", collectionName = "workitems",
          connectionStringSetting = "AzureCosmosDBConnection") final OutputBinding<String> document) {

    return processStoreRequest(request, context, document);
  }

  private static HttpResponseMessage processStoreRequest(
      final HttpRequestMessage<Optional<String>> request, final ExecutionContext context,
      final OutputBinding<String> document) {
    request.getBody().ifPresent(body -> {
      context.getLogger().info(body);
      document.setValue(body);
    });

    return request.createResponseBuilder(HttpStatus.OK).build();
  }

}
