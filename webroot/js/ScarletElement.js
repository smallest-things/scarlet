export class ScarletElement extends HTMLElement {

  constructor() {
    super()
    this.styleSheets = []
  }

  item(selectors) {
    return this.shadowRoot.querySelector(selectors)
  }

  items(selectors) {
    return this.shadowRoot.querySelectorAll(selectors)
  }

  initialize() {
    this.render()
  }

  connectedCallback() {
    this.attachShadow({mode: 'open'})
    this.initialize()
    this.shadowRoot.adoptedStyleSheets = this.styleSheets
  }

  render() {
    return this.html(`<div>ScarletElement</div>`)
  }

  html(content) {
    this.shadowRoot.innerHTML = content
  }

}

