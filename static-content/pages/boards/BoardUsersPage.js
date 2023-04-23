import buttonWithRef from "../components/ButtonWithRef.js";
import {br, div, h1, li, p, ul} from "../components/dom/domTags.js";


async function BoardUsersPage(state) {
    const items = ['id', 'name', 'email'];
    const users = state.body['users']

    return div(
        h1("Users In Board: "),
        ul(
            ...users.map(user => li(`User = ${user[`id`]}-${user[`name`]}`))
        ),
    )
}


export default BoardUsersPage;