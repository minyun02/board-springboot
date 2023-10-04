const eventTarget = document.querySelectorAll('.my-nav-link');
eventTarget.forEach((element) => {
    element.addEventListener('click', () => {
        eventTarget.forEach(e => {
            e.classList.remove('current')
        });
        element.classList.add('current');

        const postContainer = document.getElementById('post-container');
        const commentContainer = document.getElementById('comment-container');
        if (element.textContent === '게시글') {
            postContainer.style.display = 'block';
            commentContainer.style.display = 'none';
        } else {
            postContainer.style.display = 'none';
            commentContainer.style.display = 'block';
        }
    });
});