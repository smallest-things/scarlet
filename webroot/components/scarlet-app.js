import { ScarletElement } from '../js/ScarletElement.js'

class ScarletApp extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
  }

  initialize() {
    this.render()
    this.addEventListener('click-nav-bar', this.onMessage)
  }

  render() {
    return this.html(`
      <style>
        .main {
          padding: 10px;
          /*max-width: 960px;*/
          width: 100%;
          /*margin: 100px auto 500px;*/
          font-family: "Source Sans Pro", "Helvetica Neue", Arial, sans-serif;
        }
      </style>

      <section  id="application" class="container main">

        <scarlet-title title="Scarlet" subtitle="(dwarf dragonfly)"></scarlet-title>

        <scarlet-nav-bar></scarlet-nav-bar>

      </section>
		`)
  }

  onMessage(message) {
    this.item('scarlet-title').onMessage(message)
  }

}

customElements.define('scarlet-app', ScarletApp)
