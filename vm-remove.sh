#!/usr/bin/env bash

#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

multipass delete ${vm_name}
multipass purge

rm  hosts.config
