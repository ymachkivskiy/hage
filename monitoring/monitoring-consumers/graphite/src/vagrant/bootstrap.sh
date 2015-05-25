#!/usr/bin/env bash

####################
# INSTALL PACKAGES #
####################
apt-get update
apt-get install -y python python-dev python-pip \
	pkg-config apache2 libapache2-mod-wsgi libapache2-mod-python \
	libcairo2-dev python-cairo fontconfig
pip install django django-tagging pytz pyparsing \
	whisper carbon graphite-web 

export GRAPHITE_HOME=/opt/graphite



####################
# CONFIGURE CARBON #
####################
echo "Configuring Carbon"
cd $GRAPHITE_HOME/conf
cp carbon.conf.example carbon.conf
cp /vagrant/storage-schemas.conf storage-schemas.conf
echo "Done"

####################
# CONFIGURE APACHE #
####################
echo "Configuring Apache"
cp /vagrant/apache-config /etc/apache2/sites-available/default
cp $GRAPHITE_HOME/conf/graphite.wsgi.example $GRAPHITE_HOME/conf/graphite.wsgi
mkdir -p /etc/httpd/wsgi
/etc/init.d/apache2 reload
echo "Done"

######################
# CONFIGURE GRAPHITE #
######################
echo "Configuring Graphite"
cd $GRAPHITE_HOME/webapp/graphite/
cp local_settings.py.example local_settings.py
python manage.py syncdb --noinput
chown -R www-data:www-data $GRAPHITE_HOME/storage/
/etc/init.d/apache2 restart
echo "Done"

#############################
# START CARBON #
#############################
echo "Starting Carbon"
cd $GRAPHITE_HOME
./bin/carbon-cache.py start
echo "Done"










