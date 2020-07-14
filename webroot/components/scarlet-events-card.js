Vue.component(`scarlet-events-card`, {
  template: `
  <div class="card">
    <header class="card-header">
      <p class="card-header-title">
        {{title}}
      </p>
    </header>
    <div class="card-content">
      <div class="content">

        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Language</th>
              <th>Version</th>
            </tr>
          </thead>

          <tbody v-for="event in events">
            <tr>
              <th>{{event.name}}</th>
              <td>{{event.language}}</td>
              <td>{{event.version}}</td>
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
      title: "Events",
      events: null
    }
  },
  methods: {
    populateEventsList: function(events)  {
      this.events = events
    }
  },
  mounted() {
    console.log("⚡️ the scarlet-events-card vue is mounted")
    //TODO: authentication and token
    fetch('/events')
      .then(response => response.json())
      .then(data => {
        //window.events = data.events
        this.populateEventsList(data.events)
      })
    this.$root.$emit("ping", "events loaded")
  }
})

