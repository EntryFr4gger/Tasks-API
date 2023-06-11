import {AddUserToBoardFetch} from "../../components/api/fetch/boards/AddUserToBoardFetch";
import {CreateBoardFetch} from "../../components/api/fetch/boards/CreateBoardFetch";
import {DeleteBoardFetch} from "../../components/api/fetch/boards/DeleteBoardFetch";
import {GetBoardCardsArchivedFetch, GetBoardCardsFetch} from "../../components/api/fetch/boards/GetBoardCardsFetch";
import {GetBoardDetailsFetch} from "../../components/api/fetch/boards/GetBoardDetailsFetch";
import {GetBoardListsFetch} from "../../components/api/fetch/boards/GetBoardListsFetch";
import {GetBoardUsersFetch} from "../../components/api/fetch/boards/GetBoardUsersFetch";
import {SearchBoardsFetch} from "../../components/api/fetch/boards/SearchBoardsFetch";

const bearerToken = "Bearer 9f1e3d11-8c18-4cd7-93fc-985c4794cfd9"
jest.mock("../../../static-content/components/utils/get-token.js", () => ({
    getUserToken: jest.fn(() => bearerToken),
}));
global.fetch = jest.fn().mockResolvedValue({
    ok: true,
    status: 200,
    json: jest.fn().mockResolvedValue({}),
});

describe("Board Fetch Tests", () => {

    it("Add User To Board Fetch", async () => {
        const boardId = 1;
        const userId = 3;

        await AddUserToBoardFetch(boardId, userId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}/users`, {
            method: "POST",
            headers: { Authorization: bearerToken },
            body: JSON.stringify({ id: userId }),
        });

    });

    it("Create Board Fetch", async () => {
        const name = "NewBoardName";
        const description = "VeryDescriptions";

        await CreateBoardFetch(name, description);

        expect(fetch).toHaveBeenCalledWith(`boards/`, {
            method: "POST",
            headers: { Authorization: bearerToken },
            body: JSON.stringify({ name: name, description:description }),
        });
    });

    it("Delete Board Fetch", async () => {
        const boardId = 1;

        await DeleteBoardFetch(boardId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}`, {
            method: "DELETE",
            headers: { Authorization: bearerToken }
        });
    });

    it("Get Board Cards Fetch", async () => {
        const boardId = 1;

        await GetBoardCardsArchivedFetch(boardId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}/cards?archived`, {
            headers: { Authorization: bearerToken }
        });
    });

    it("Get Board Details Fetch", async () => {
        const boardId = 1;

        await GetBoardDetailsFetch(boardId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}`, {
            headers: { Authorization: bearerToken }
        });
    });

    it("Get Board Details Fetch", async () => {
        const boardId = 1;

        await GetBoardDetailsFetch(boardId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}`, {
            headers: { Authorization: bearerToken }
        });
    });

    it("Get Board Lists Fetch", async () => {
        const boardId = 1;
        const skip = 1;
        const limit = 1;
        await GetBoardListsFetch(boardId,1,1);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}/lists?skip=${skip}&limit=${limit}`, {
            headers: { Authorization: bearerToken }
        });
    });

    it("Get Board Users Fetch", async () => {
        const boardId = 1;
        await GetBoardUsersFetch(boardId);

        expect(fetch).toHaveBeenCalledWith(`boards/${boardId}/users`, {
            headers: { Authorization: bearerToken }
        });
    });

    it("Search Boards Fetch", async () => {
        const name = "Test";
        const skip = 1;
        const limit = 1;
        await SearchBoardsFetch(name,skip,limit);

        expect(fetch).toHaveBeenCalledWith(`boards/?name=${name}&skip=${skip}&limit=${limit}`, {
            headers: { Authorization: bearerToken },
        });
    });


});