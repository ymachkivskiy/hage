jAgE Build Configurations README
================================

This module is *not* meant to be built and deployed with the rest of the platform.
The reasoning behind this is that it changes very rarely. Its snapshots would be
the same most of the time. Because of that, this module is not included in the
<modules/> list of the root POM.

If you need to change any of configuration files from this module, please bump its
version as needed and change the version of the dependency in the root POM before
deployment.
