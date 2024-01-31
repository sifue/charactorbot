#!/bin/bash
# Project deploy script for Mac OS X 10.7

echo "-------  Download Google App Engine SDK for Java"
curl -O http://googleappengine.googlecode.com/files/appengine-java-sdk-1.7.3.zip

echo "-------  Unzip Google App Engine SDK for Java"
unzip appengine-java-sdk-1.7.3.zip
rm appengine-java-sdk-1.7.3.zip

echo "-------  Update charactorbot project."
appengine-java-sdk-1.7.3/bin/appcfg.sh update war

echo "-------  Clean Google App Engine SDK for Java"
rm -r appengine-java-sdk-1.7.3