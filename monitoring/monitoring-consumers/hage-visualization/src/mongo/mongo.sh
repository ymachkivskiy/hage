#!/usr/bin/env bash

####################
# INSTALL PACKAGES #
####################

apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10
echo "deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen" | tee -a /etc/apt/sources.list.d/10gen.list
apt-get -y update
apt-get -y install mongodb-10gen

######################
# CONFIGURE MONGODB #
######################

sudo mkdir -p /data/db/
sudo chown `id -u` /data/db

######################
# CONFIGURE MONGOD #
######################

mongod&
