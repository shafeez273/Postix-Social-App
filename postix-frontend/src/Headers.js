function makeBasic(auth) {
    return "Basic " + btoa(auth.name + ":" + auth.password);
}
export function basic(auth) {
    return {"Authorization": makeBasic(auth)};
}

export function anonJson() {
    return {"Content-Type": "application/json"}
}

export function basicJson(auth) {
    return {...basic(auth), ...anonJson()}
}
