# [Choice] Java version: 8, 11, 15
ARG VARIANT=11
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

# Install Maven
ARG MAVEN_VERSION=""
RUN su vscode -c "source /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""

# Install additional OS packages: Azure CLI && Azure Functions Core Tools
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash && \
    curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg && \
    sudo mv microsoft.gpg /etc/apt/trusted.gpg.d/microsoft.gpg && \
    sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/debian/$(lsb_release -rs | cut -d'.' -f 1)/prod $(lsb_release -cs) main" > /etc/apt/sources.list.d/dotnetdev.list' && \
    sudo apt-get update && \
    sudo apt-get install azure-functions-core-tools-3
