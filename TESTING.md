# Testing Scarlet

## With Multipass

If you need a safe/clean environment to execute **Scarlet**, first install [**Multipass**](https://multipass.run/).

Then build locally the **Scarlet** project

```bash
mvn clean package
```

Then create the virtual machine with this scirpt: `./vm-create.sh` (and wait for some minutes ⏳)

> - stop the vm: `./vm-stop.sh`
> - (re)start the vm: `./vm-start.sh`
> - destroy the vm: `./vm-remove.sh`
> - (re)create the vm: `./vm-create.sh`
> - open a shell prompt on the vm: `./vm-shell.sh`

🖐️ `./vm-create.sh` create a VM (with GraalVM and guest languages) and mount a volume mapped on the `/target` directory of the host. (the `/target` must exist, don't forget to build the project)

🖐️ **the script create a file (`hosts.config`) where you can find the IP address of the VM**

> You can retrieve the IP address of the VM with this command:
> ```bash
> IP=$(multipass info ${vm_name} | grep IPv4 | awk '{print $2}')
> ```

Once the VM created, open a shell (`./vm-shell.sh`) and type:

```bash
java -jar target/scarlet-0.0.0-SNAPSHOT-fat.jar
```

Now, on the host side, you can access to the REST API like that:

```bash
export DOMAIN="192.168.64.96"; export HTTP_PORT=8080; ./create_all_functions.sh
```

> use the appropriate IP address 😉
