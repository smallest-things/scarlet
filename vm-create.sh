#!/usr/bin/env bash
SECONDS=0

#--------------------------------------------------
# Read environment variables
#--------------------------------------------------
set -o allexport
source ./.env

multipass launch focal --name ${vm_name} --cpus ${vm_cpus} --mem ${vm_mem} --disk ${vm_disk} \
	--cloud-init ./cloud-init.yaml

IP=$(multipass info ${vm_name} | grep IPv4 | awk '{print $2}')

echo "👋 Initialize ${vm_name}..."

multipass mount target ${vm_name}:target
multipass info ${vm_name}

multipass exec ${vm_name} -- sudo -- sh -c "echo \"${IP} ${vm_domain}\" >> /etc/hosts"

# install Graal VM
multipass --verbose exec ${vm_name} -- bash <<EOF
wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${graalvm_version}/graalvm-ce-java8-linux-amd64-${graalvm_version}.tar.gz
tar -xvzf graalvm-ce-java8-linux-amd64-${graalvm_version}.tar.gz
EOF

# install oh-my-bash
multipass exec ${vm_name} -- bash <<EOF
curl https://raw.githubusercontent.com/ohmybash/oh-my-bash/master/tools/install.sh --output install.sh
chmod +x install.sh
./install.sh
rm install.sh
EOF

multipass --verbose exec ${vm_name} -- bash <<EOF
echo "export JAVA_HOME=~/graalvm-ce-java8-${graalvm_version}" >> ~/.bashrc
echo 'export PATH=\$PATH:\$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
java -version
gu install python
gu install ruby
EOF

echo "${IP} ${vm_domain}" > ./hosts.config

duration=$SECONDS
echo "Duration: $(($duration / 60)) minutes and $(($duration % 60)) seconds elapsed."
