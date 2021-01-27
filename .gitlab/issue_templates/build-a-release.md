
### Release process (build a release)

- [ ] add the related issues to this issue (*the issues with a label like this one ~"0.0.0"*)
- [ ] add a version label: ~"0.0.0" to this issue (create the label if it doesn't exist)
- [x] add the ~release label (*label applied automatically when creating the issue*)

When all the issues are closed:

- [ ] create an MR from this issue
- [ ] change the release version in the `pom.xml` file (`x.y.z-SNAPSHOT` to `x.y.z`)
- [ ] create a tag from the branch of the MR (it will trigger the creation of the release)

Then merge the MR on the `master` branch

And
- initialize the next release by creating a new issue (use the `initialize-the-next-release.md` template)
- create a new MR from this issue
- increment `x.y.z` and add `-SNAPSHOT` in the `pom.xml` file

Then merge the MR on the `master` branch

/label ~release
/assign me
