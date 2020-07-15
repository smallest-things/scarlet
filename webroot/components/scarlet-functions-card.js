import { ScarletElement } from '../js/ScarletElement.js'

class ScarletFunctionsCard extends ScarletElement {

  constructor() {
    super()
    this.styleSheets = [window.chota]
    this.functions = []
  }

  getData()  {
    return fetch('/functions')
      .then(response => response.json())
      .then(data => {
        this.functions = data.functions
      })
  }

  initialize() {
    this.getData().then(_ => this.render())
    this.timer = setInterval(() => this.getData().then(_ => this.render()), 3000)
  }

  getRows(functions) {
    return functions.map(func => `
        <tr>
            <th>${func.name}</th>
            <td>${func.language}</td>
            <td>${func.version}</td>
        </tr>
        `
    ).join("")
  }

  render() {
    return this.html(`
      <div class="card">
        <header>
          <h4>Functions</h4>
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
              ${this.getRows(this.functions)}
            </tbody>
          </table>

        <footer class="is-right">
          <!--footer-->
        </footer>
      </div>
		`)
  }

}

customElements.define('scarlet-functions-card', ScarletFunctionsCard)
