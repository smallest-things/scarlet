#!/bin/bash

#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

multipass start ${vm_name}
multipass info ${vm_name}
