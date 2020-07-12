#!/usr/bin/env bash

#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

multipass stop ${vm_name}
