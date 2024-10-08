<script>
    import { onMount,onDestroy } from 'svelte';
    import Chart from 'chart.js/auto'
    import { idSoc } from './store.js';
    
    let selectedSocietyIds = [];

    $: if ($idSoc && !selectedSocietyIds.includes($idSoc)) {
      selectedSocietyIds = [...selectedSocietyIds, $idSoc];
    }

    let chart;
    let chartElement;
    let data = {};
    const maxDataPoints = 20;
    let eventSources = {};

    const colors = [
      'rgb(255, 99, 132)',
      'rgb(54, 162, 235)',
      'rgb(255, 206, 86)',
      'rgb(75, 192, 192)',
      'rgb(153, 102, 255)',
      'rgb(255, 159, 64)'
    ];

    $: {
      selectedSocietyIds.forEach(id => {
        if (!eventSources[id]) {
          data[id] = [];
          eventSources[id] = new EventSource(`http://localhost:8080/stream/transactions/societe/${id}`);
          eventSources[id].onmessage = (event) => {
            let price = JSON.parse(event.data).price;
            updateChart(id, price);
          };
          eventSources[id].onerror = (error) => {
            console.error(`EventSource failed for society ${id}:`, error);
            eventSources[id].close();
            delete eventSources[id];
          };
        }
      })
    }

    function updateChart(societyId, price){
      if (data[societyId].length > maxDataPoints) {
        data[societyId].shift();
      }
      data[societyId].push(price);
      if (chart) {
        chart.data.labels = Array.from({length: maxDataPoints}, (_, i) => i.toString());
        chart.data.datasets = Object.entries(data).map(([id, values], index) => ({
          label: `Société ${id}`,
          data: values,
          borderColor: colors[index % colors.length],
          fill: false
        }));
        chart.update();
      }
    }

    onMount(() => {
      chart = new Chart(chartElement, {
        type: 'line',
        data: {
          labels: [],
          datasets: []
        },
        options: {
          responsive: true,
          animation: {
            duration: 0
          },
          scales: {
            y: {
              beginAtZero: true,
              max: 1000
            }
          }
        }
      });
    });


    onDestroy(() => {
      onDestroy(() => {
        Object.values(eventSources).forEach(es => es.close());
        if (chart) {
          chart.destroy();
        }
      });
  });
</script>

<canvas bind:this={chartElement}></canvas>