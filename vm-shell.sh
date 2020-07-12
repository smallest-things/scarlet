#!/usr/bin/env bash
#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

multipass shell ${vm_name}

