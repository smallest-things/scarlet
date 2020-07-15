// 🚧 WIP

let match = (uri) => { //using hash
  uri = uri.replace("#\/","");
  let uriParts = uri.split("/").filter((part)=>part.length>0);
  let route = uriParts[0];
  let params = uriParts.slice(1);
  console.log(route, params)
}

window.onpopstate = (event) => {
  match(window.location.hash);
}
