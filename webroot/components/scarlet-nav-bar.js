import { ScarletElement } from '../js/ScarletElement.js'

class ScarletNavBar extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
  }

  initialize() {
    this.render()

    this.item(".functions").addEventListener('click', event => this.onClick("functions"))
    this.item(".events").addEventListener('click', event => this.onClick("events"))
    this.item(".subscriptions").addEventListener('click', event => this.onClick("subscriptions"))

    this.functionsCard = this.item("scarlet-functions-card")
    this.eventsCard = this.item("scarlet-events-card")
    this.subscriptionsCard = this.item("scarlet-subscriptions-card")

    this.functionsCard.style.display = "block"
    this.eventsCard.style.display = "none"
    this.subscriptionsCard .style.display = "none"
  }

  onClick(event) {
    this.dispatchEvent(
      new CustomEvent('click-nav-bar', {
        bubbles: true, composed: true, detail: {
          value: event
        }
      })
    )

    if(event==="functions") {
      this.functionsCard.style.display = "block"
      this.eventsCard.style.display = "none"
      this.subscriptionsCard.style.display = "none"
    }

    if(event==="events") {
      this.eventsCard.style.display = "block"
      this.functionsCard.style.display = "none"
      this.subscriptionsCard.style.display = "none"
    }

    if(event==="subscriptions") {
      this.subscriptionsCard.style.display = "block"
      this.functionsCard.style.display = "none"
      this.eventsCard.style.display = "none"
    }
  }

  render() {
    return this.html(`
      <nav class="tabs is-center">
        <a href="#functions" class="functions"><b>Functions</b></a>
        <a href="#events" class="events"><b>Events</b></a>
        <a href="#subscriptions" class="subscriptions"><b>Subscriptions</b></a>
      </nav>

      <scarlet-functions-card hidden=false></scarlet-functions-card>
      <scarlet-events-card hidden=true></scarlet-events-card>
      <scarlet-subscriptions-card hidden=true></scarlet-subscriptions-card>
		`)
  }
}

customElements.define('scarlet-nav-bar', ScarletNavBar)
