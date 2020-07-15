import { ScarletElement } from '../js/ScarletElement.js'

class ScarletSubscriptionsCard extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
    this.subscriptions = []
  }

  getData()  {
    return fetch('/subscriptions')
      .then(response => response.json())
      .then(data => {
        this.subscriptions = data.subscriptions
      })
  }

  initialize() {
    this.getData().then(_ => this.render())
    this.timer = setInterval(() => this.getData().then(_ => this.render()), 3000)
  }

  getRows(subscriptions) {
    return subscriptions.map(subscription => `
        <tr>
            <th>${subscription.id}</th>
            <td>${subscription.topic}</td>
        </tr>
        `
    ).join("")
  }

  render() {
    return this.html(`
      <div class="card">
        <header>
          <h4>Subscriptions</h4>
        </header>

          <table>
            <thead>
              <tr>
                <th>Id</th>
                <th>Topic</th>
              </tr>
            </thead>

            <tbody>
              ${this.getRows(this.subscriptions)}
            </tbody>
          </table>

        <footer class="is-right">
          <!--footer-->
        </footer>
      </div>
		`)
  }

}

customElements.define('scarlet-subscriptions-card', ScarletSubscriptionsCard)
