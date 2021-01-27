
### Release process (initialize the next release)

- [ ] add a version label: ~"0.0.0" to this issue (create the label if it doesn't exist)
- [x] add the ~release label (*label applied automatically when creating the issue*)
- [ ] create an MR from this issue
- [ ] change the release version in the `pom.xml` file (`x.y.z` to `x.y.z-SNAPSHOT`)

Then merge the MR on the `master` branch

/label ~release
/assign me
