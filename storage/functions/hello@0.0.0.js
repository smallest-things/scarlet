function hello(params) {
  return {
    message: "Hello World",
    total: 42,
    author: "@k33g_org",
    params: params.getString("name")
  }
}