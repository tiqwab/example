#!/bin/sh

PACKAGED_COOKBOOKS_NAME=zabbix-opsworks-cookbooks.tar.gz
S3_BUCKET=s3://zabbix-opsworks-cookbooks/$PACKAGED_COOKBOOKS_NAME

# Package all cookbooks
berks package $PACKAGED_COOKBOOKS_NAME

# Upload to S3 bucket
aws s3 cp $PACKAGED_COOKBOOKS_NAME $S3_BUCKET

rm $PACKAGED_COOKBOOKS_NAME
