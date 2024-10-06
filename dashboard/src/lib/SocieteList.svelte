<script>
    import { onMount } from 'svelte';

    let societies = [];

    onMount(async () => {
        const response = await fetch('http://localhost:8080/societs');
        societies = await response.json();
    });
</script>

<style>
    .society-list {
        display:flex;
        flex-wrap: wrap;
        gap: 1rem;
        padding: 1rem;
    }

    .society-item {
        width: 15%;
        padding: 0.5rem;
        font-size: x-small;
        border: 1px solid #ccc;
        border-radius: 8px;
        background-color: #347ae494;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .society-item h2 {
        margin: 0;
        font-size: 1.5rem;
        color: #333;
    }

    .society-item p {
        margin: 0.5rem 0 0;
        color: #666;
    }
</style>

<div class="society-list">
    {#each societies as society}
        <div class="society-item">
            <h3>{society.name}</h3>
        </div>
    {/each}
</div>