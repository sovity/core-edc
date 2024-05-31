<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url] [![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Apache 2.0][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<br />
<div align="center">
    <a href="https://github.com/sovity/edc-extensions">
        <img src="https://raw.githubusercontent.com/sovity/edc-ui/main/src/assets/images/sovity_logo.svg" alt="Logo" width="300">
    </a>
</div>

<h1 align="center">
  <br/>
      EDC Connector (fork)
  <br/>
</h1>

## About The Project

This is a fork of the [Eclipse Dataspace Connector](https://github.com/eclipse-edc/Connector) by [sovity](https://sovity.de/).

## sovity Eclipse Data Components fork

This repository contains forks of the Eclipse EDC with additions by sovity.

Each branch has its own changelog.
Have a look at each branch for more details:

<a name="branches"></a>

[0.2.1](https://github.com/sovity/core-edc/blob/sovity/0.2.1/CHANGELOG.md)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Compatibility

This fork is meant to be a drop-in replacement for the Eclipse EDC dependencies and therefore re-uses the same artifact group name.

This is meant to save time and avoid editing and re-compiling the code of all the dependencies that may use any of the components of this repository.

Each branch under `sovity/` is a different fork version and may contain different patches. Each of these branches therefore has a different changelog.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

The `main` branch tracks the **Eclipse EDC** `main` branch. It is not used as the main branch in this fork.

The `default` branch serves as a **main branch** for this fork. It contains the common elements: READMEs, CI etc.

The **forked branches** are prefixed with `sovity/`.

Any version that needs forking should be labeled `sovity/X.Y.Z` where `X.Y.Z` is the Eclipse EDC version from which it was forked and acts as the main branch for this forked version.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Releasing

The artifacts for this repository are not available on Github but on Azure.

The repo's URL is:

https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1

The artifacts are released under the same group id as the original EDC: `org.eclipse.edc` and the same artifact names. Only the version differs.

## Contributing

This is already a set of workarounds and patches, diverging from the original repository. Some patches have been backported from later EDC versions.

To submit changes to the Eclipse EDC, please refer to [the original repository](https://github.com/eclipse-edc/Connector)

## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

contact@sovity.de

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]:
https://img.shields.io/github/contributors/sovity/core-edc.svg?style=for-the-badge

[contributors-url]: https://github.com/sovity/core-edc/graphs/contributors

[forks-shield]:
https://img.shields.io/github/forks/sovity/core-edc.svg?style=for-the-badge

[forks-url]: https://github.com/sovity/core-edc/network/members

[stars-shield]:
https://img.shields.io/github/stars/sovity/core-edc.svg?style=for-the-badge

[stars-url]: https://github.com/sovity/core-edc/stargazers

[issues-shield]:
https://img.shields.io/github/issues/sovity/core-edc.svg?style=for-the-badge

[issues-url]: https://github.com/sovity/core-edc/issues

[license-shield]:
https://img.shields.io/github/license/sovity/core-edc.svg?style=for-the-badge

[license-url]: https://github.com/sovity/core-edc/blob/main/LICENSE

[linkedin-shield]:
https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://www.linkedin.com/company/sovity
