#!/usr/bin/env bash
SECONDS=0

#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

IP=$(multipass info ${vm_name} | grep IPv4 | awk '{print $2}')

multipass mount target ${vm_name}:target
multipass info ${vm_name}
