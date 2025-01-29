function getAuthToken() {
    const authJson = localStorage.getItem('auth')
    if (!authJson) {
        return null
    }

    return JSON.parse(authJson).token
}

export const httpOptions = {
    prefixUrl: 'http://localhost:8080/api',
};

export const authHttpOptions = {
    prefixUrl: 'http://localhost:8080/api',
    hooks: {
        beforeRequest: [
            async (request: Request) => {
                const token = getAuthToken();
                request.headers.set('Authorization', `Bearer ${token}`);
            },
        ],
    }
}