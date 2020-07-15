import { ScarletElement } from '../js/ScarletElement.js'

class ScarletTitle extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
  }

  initialize() {
    this.render()
  }

  render() {
    return this.html(`
      <div>
        <h1>${this.getAttribute("title")}</h1>
        <h2>${this.getAttribute("subtitle")}</h2>
      </div>
		`)
  }

  onMessage(message) {
    if(message.type==='click-nav-bar') {
      console.log("ScarletTitle 📩", message.detail.value)
    }
  }

}

customElements.define('scarlet-title', ScarletTitle)
