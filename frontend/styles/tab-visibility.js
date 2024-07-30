document.addEventListener('visibilitychange', function() {
    if (document.visibilityState === 'visible') {
        location.reload();
    }
});