Vue.component(`scarlet-panel`, {
  template: `
    <div id="tabs-with-content">

      <div class="tabs">
        <ul>
          <li class="is-active"><a>Functions</a></li>
          <li><a>Events</a></li>
          <li><a>Subscriptions</a></li>
        </ul>
      </div>
      <div>
        <section class="tab-content"><scarlet-functions-card></scarlet-functions-card></section>
        <section class="tab-content"><scarlet-events-card></scarlet-events-card></section>
        <section class="tab-content"><scarlet-subscriptions-card></scarlet-subscriptions-card></section>
      </div>
    </div>

  `,
  mounted() {
    console.log("⚡️ the scarlet-pane vue is mounted")
    let tabs = document.querySelectorAll('.tabs li');
    let tabsContent = document.querySelectorAll('.tab-content');

    let deactivateAllTabs = _ => {
      tabs.forEach( (tab) => {
        tab.classList.remove('is-active');
      });
    };

    let hideTabsContent = _ => {
      tabsContent.forEach( (tabContent) => {
        tabContent.classList.remove('is-active');
      });
    };

    let activateTabsContent = (tab) => {
      tabsContent[getIndex(tab)].classList.add('is-active');
    };

    let getIndex = (el) => {
      return [...el.parentElement.children].indexOf(el);
    };

    tabs.forEach(function (tab) {
      tab.addEventListener('click', () => {
        deactivateAllTabs();
        hideTabsContent();
        tab.classList.add('is-active');
        activateTabsContent(tab);
      });
    })

    tabs[0].click();
  }
})
