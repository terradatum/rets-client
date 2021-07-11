# Changelog

## [1.4.0](https://github.com/terradatum/rets-client/compare/v1.3.0...v1.4.0) (2021-07-11)


### Features

* finish support for configuration of HttpClient via System Properties ([82f880a](https://github.com/terradatum/rets-client/commit/82f880a3decff5cc7e9dcac51f3bbdc6c284710d))

## [1.3.0](https://github.com/terradatum/rets-client/compare/v1.2.3...v1.3.0) (2021-07-09)


### Features

* use `SystemDefaultHttpClient` to support configuration via system properties ([012016f](https://github.com/terradatum/rets-client/commit/012016f09244b01dfd974e231fee056fac851cc1))

### [1.2.3](https://github.com/terradatum/rets-client/compare/v1.2.2...v1.2.3) (2021-06-23)


### Bug Fixes

* **ci:** use the correct JDK for building ([97c1215](https://github.com/terradatum/rets-client/commit/97c121504844cfa9bb64b4b2b1a73fe945e5bcf8))
* **user-agent:** ensure there is always a "User-Agent" header - previously allowed null ([9ff0317](https://github.com/terradatum/rets-client/commit/9ff03170775006acf5456b7814b0a883491d57b1))

### [1.2.2](https://github.com/terradatum/rets-client/compare/v1.2.1...v1.2.2) (2021-06-17)


### Bug Fixes

* **ci:** run the `@semantic-release/git` plugin AFTER the `@semantic-release/exec` plugin ([90d1c3f](https://github.com/terradatum/rets-client/commit/90d1c3f277132bdb87a3e7e88389c184408df2b1))

### [1.2.1](https://github.com/terradatum/rets-client/compare/v1.2.0...v1.2.1) (2021-06-17)


### Bug Fixes

* turn down the logging levels ([2b55d06](https://github.com/terradatum/rets-client/commit/2b55d06e45b5c73569084aaa046d4bb58c120d0f))

## [1.2.0](https://github.com/terradatum/rets-client/compare/v1.1.4...v1.2.0) (2021-06-16)


### Features

* LoginResponse - update to 1.8/1.9 and fix unit tests ([357797e](https://github.com/terradatum/rets-client/commit/357797ea9179805edd29922c636003d979e75ac2))

### [1.1.4](https://github.com/terradatum/rets-client/compare/v1.1.3...v1.1.4) (2021-06-13)


### Bug Fixes

* align the current version with the tracked releases ([2c11cbc](https://github.com/terradatum/rets-client/commit/2c11cbcae1816d8c43a36e66e56fc41f65b67651))

### [1.1.3](https://github.com/terradatum/rets-client/compare/v1.1.2...v1.1.3) (2021-06-12)


### Bug Fixes

* should be able to delete the release artifact after it's been published? ([bfcd5be](https://github.com/terradatum/rets-client/commit/bfcd5bee6fc31b05b4fe9eb0a126921a4c613240))

### [1.1.2](https://github.com/terradatum/rets-client/compare/v1.1.1...v1.1.2) (2021-06-12)


### Bug Fixes

* add zip extension to release artifact ([c2cdbab](https://github.com/terradatum/rets-client/commit/c2cdbab0030e2021ae05a799ea3964a80b64257e))
* update semantic-release configuration ([892fc4e](https://github.com/terradatum/rets-client/commit/892fc4ebf4e16f04931f8e8e611d11f3fb5efbc4))

### [1.1.1](https://github.com/terradatum/rets-client/compare/v1.1.0...v1.1.1) (2021-06-11)


### Bug Fixes

* fix typos and misnamed assets for release packaging ([5ca7261](https://github.com/terradatum/rets-client/commit/5ca72617b672cc099c882a652f28834e7c8e8a14))

## [1.1.0](https://github.com/terradatum/rets-client/compare/v1.0.1...v1.1.0) (2021-06-11)


### Features

* package the artifacts for release ([3319699](https://github.com/terradatum/rets-client/commit/3319699e6f656d9960020a4a24038108948ad2a6))

### [1.0.1](https://github.com/terradatum/rets-client/compare/v1.0.0...v1.0.1) (2021-06-11)


### Bug Fixes

* add conventional-changelog-conventionalcommits to the extra_plugins used by semantic-release ([bc78e75](https://github.com/terradatum/rets-client/commit/bc78e7572cd447d61b08ac8a1a9bec6d734dd455))
* correct name in main.yml workflow ([532ec49](https://github.com/terradatum/rets-client/commit/532ec49b988936dd118cb20696290c12afc8e5a6))
* use a more traditional workflow that uses only GITHUB_TOKEN (as opposed to a PAT) ([e47da20](https://github.com/terradatum/rets-client/commit/e47da20ca2e6c606c1b6b8f6f56a486aaa724e92))
* use a more traditional workflow that uses only GITHUB_TOKEN (as opposed to a PAT) ([3db33ba](https://github.com/terradatum/rets-client/commit/3db33ba9ce81afef0c0f6fa080f0d17af48c82e4))
* use the correct branch ([ec85c78](https://github.com/terradatum/rets-client/commit/ec85c7801095534ab4836798cf03848b2983513e))
* use the correct GitHub token ([7103b17](https://github.com/terradatum/rets-client/commit/7103b1708a48eb9ee389c5bea835c905a20370ef))

## 1.0.0 (2021-06-11)


### Bug Fixes

* add conventional-changelog-conventionalcommits to the extra_plugins used by semantic-release ([bc78e75](https://github.com/terradatum/rets-client/commit/bc78e7572cd447d61b08ac8a1a9bec6d734dd455))
* correct name in main.yml workflow ([532ec49](https://github.com/terradatum/rets-client/commit/532ec49b988936dd118cb20696290c12afc8e5a6))
* use a more traditional workflow that uses only GITHUB_TOKEN (as opposed to a PAT) ([e47da20](https://github.com/terradatum/rets-client/commit/e47da20ca2e6c606c1b6b8f6f56a486aaa724e92))
* use a more traditional workflow that uses only GITHUB_TOKEN (as opposed to a PAT) ([3db33ba](https://github.com/terradatum/rets-client/commit/3db33ba9ce81afef0c0f6fa080f0d17af48c82e4))
* use the correct branch ([ec85c78](https://github.com/terradatum/rets-client/commit/ec85c7801095534ab4836798cf03848b2983513e))
* use the correct GitHub token ([7103b17](https://github.com/terradatum/rets-client/commit/7103b1708a48eb9ee389c5bea835c905a20370ef))
