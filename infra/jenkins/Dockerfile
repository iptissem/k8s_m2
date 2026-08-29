FROM jenkins/jenkins:lts-jdk21

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# kubectl
RUN curl -L -o /usr/local/bin/kubectl \
    https://dl.k8s.io/release/v1.37.0/bin/linux/amd64/kubectl \
    && chmod +x /usr/local/bin/kubectl

# Helm
RUN curl -L -o /tmp/helm.tar.gz \
    https://get.helm.sh/helm-v4.2.4-linux-amd64.tar.gz \
    && tar -xzf /tmp/helm.tar.gz -C /tmp \
    && mv /tmp/linux-amd64/helm /usr/local/bin/helm \
    && chmod +x /usr/local/bin/helm \
    && rm -rf /tmp/linux-amd64 /tmp/helm.tar.gz

USER jenkins