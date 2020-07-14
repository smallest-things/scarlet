Vue.component(`scarlet-functions-card`, {
  template: `
  <div class="card">
    <header class="card-header">
      <p class="card-header-title">
        {{title}}
      </p>
    </header>
    <div class="card-content">
      <div class="content">
        <!--
        <div v-for="func in functions">
          <h2 class="subtitle">{{func.name}}</h2>
        </div>
        -->
        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Language</th>
              <th>Version</th>
            </tr>
          </thead>

          <tbody v-for="func in functions">
            <tr>
              <th>{{func.name}}</th>
              <td>{{func.language}}</td>
              <td>{{func.version}}</td>
            </tr>
          </tbody>
        </table>

      </div>
    </div>
    <footer class="card-footer">
      <!--footer-->
    </footer>
  </div>
  `,
  data() {
    return {
      title: "Functions",
      functions: null,
      timer:null
    }
  },
  methods: {
    populateFunctionsList: function()  {
      fetch('/functions')
        .then(response => response.json())
        .then(data => {
          this.functions = data.functions
        })
    }
  },
  mounted() {
    console.log("⚡️ the scarlet-functions-card vue is mounted")
    //TODO: authentication and token
    this.$root.$emit("ping", "functions loaded")

    this.timer = setInterval(this.populateFunctionsList, 3000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  }

})


