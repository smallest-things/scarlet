import { ScarletElement } from '../js/ScarletElement.js'

class ScarletEventsCard extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
    this.events = []
  }

  getData()  {
    return fetch('/events')
      .then(response => response.json())
      .then(data => {
        this.events = data.events
      })
  }

  initialize() {
    this.getData().then(_ => this.render())
    this.timer = setInterval(() => this.getData().then(_ => this.render()), 3000)
  }

  getRows(events) {
    return events.map(event => `
        <tr>
            <th>${event.name}</th>
            <td>${event.language}</td>
            <td>${event.version}</td>
        </tr>
        `
    ).join("")
  }

  render() {
    return this.html(`
      <div class="card">
        <header>
          <h4>Events</h4>
        </header>

          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Language</th>
                <th>Version</th>
              </tr>
            </thead>

            <tbody>
              ${this.getRows(this.events)}
            </tbody>
          </table>

        <footer class="is-right">
          <!--footer-->
        </footer>
      </div>
		`)
  }

}

customElements.define('scarlet-events-card', ScarletEventsCard)
