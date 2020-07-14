Vue.component(`scarlet-subscriptions-card`, {
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
              <th>id</th>
              <th>topic</th>
            </tr>
          </thead>

          <tbody v-for="subscription in subscriptions">
            <tr>
              <th>{{subscription.id}}</th>
              <td>{{subscription.topic}}</td>
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
      title: "Subscriptions",
      subscriptions: null,
      timer:null
    }
  },
  methods: {
    populateSubscriptionsList: function()  {
      fetch('/subscriptions')
        .then(response => response.json())
        .then(data => {
          this.subscriptions = data.subscriptions
        })
    }
  },
  mounted() {
    console.log("⚡️ the scarlet-subscriptions-card vue is mounted")
    //TODO: authentication and token
    this.$root.$emit("ping", "subscriptions loaded")

    this.timer = setInterval(this.populateSubscriptionsList, 3000)

  }
})
