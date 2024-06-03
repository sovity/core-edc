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

This is a fork of the [Eclipse EDC Connector](https://github.com/eclipse-edc/Connector) with additions by [sovity](https://sovity.de/).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Branches

- The `default` branch serves as a default branch for this fork, only containing an entry point with the main README and CI.
- The `main` branch tracks the **Eclipse EDC** `main` branch. It is not used as the main branch in this fork.
- The `sovity/x.y.z` branches will contain forked **Eclipse EDC** releases with commits on top.
    - e.g. the currently maintained 0.2.1 fork, [sovity/0.2.1](https://github.com/sovity/core-edc/blob/sovity/0.2.1/CHANGELOG.md)

Each branch has its own Changelog, as patches are always applied on top of releases and the entire history of the Eclipse EDC Connector is not maintained.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

The artifacts for this repository are not available via the GitHub Maven Repository but via our own Maven Registry on Azure. This is because the many artifacts created in this repository caused the GitHub Maven Repository to be a bottleneck for the CI. We also needed better mirroring of transitive dependencies, as transitive dependencies of the Eclipse EDC Connector have disappeared before.

The Maven Registry on Azure is public and can be added to any Maven or Gradle Project using this URL:

https://pkgs.dev.azure.com/sovity/41799556-91c8-4df6-8ddb-4471d6f15953/_packaging/core-edc/maven/v1

The artifacts are released under the same group id as the original EDC: `org.eclipse.edc` and the same artifact names. Only the version differs and usually matches the pattern `x.y.z.w` with `x.y.z` matching the Eclipse EDC version it was based on.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contributing

To submit changes to the Eclipse EDC, please refer to [the original repository](https://github.com/eclipse-edc/Connector)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Releasing

[Create a Release Issue](https://github.com/sovity/core-edc/issues/new?assignees=&labels=task%2Frelease%2Cscope%2Fcore&projects=&template=release.md&title=Release+X.Y.Z.W) and follow the instructions.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

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
