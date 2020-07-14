Vue.component(`scarlet-title`, {
  template: `
    <div>
      <h1 class="title">{{title}}</h1>
      <h2 class="subtitle">{{subTitle}}</h2>
    </div>
  `,
  data() {
    return {
      title: "Scarlet (dwarf dragonfly)",
      subTitle: ""
    }
  },
  methods:{

  },
  mounted() {
    console.log("⚡️ thescarlet-title  vue is mounted")

    this.$root.$on("ping", (message)=> {
      this.subTitle = `${message}`
    })
  }
})

