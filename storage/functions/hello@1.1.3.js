function hello(params) {
  return {
    message: "🎃 Hello World",
    params: params.getString("name")
  }
}