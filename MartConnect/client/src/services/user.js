import axios from 'axios'
// import { config } from '../config'

// export async function getProfile() {
//   try {
//     const url = `${config.serverURL}/user/profile`
//     const token = sessionStorage.getItem('token')
//     const response = await axios.get(url, {
//       headers: { token },
//     })
//     if (response.status == 200) {
//       return response.data
//     }
//   } catch (ex) {
//     console.log(`exception: `, ex)
//   }
// }

// export async function updateProfile(firstName, lastName, phone) {
//   try {
//     const url = `${config.serverURL}/user/profile`
//     const token = sessionStorage.getItem('token')
//     const body = { firstName, lastName, phone }
//     const response = await axios.put(url, body, {
//       headers: { token },
//     })
//     if (response.status == 200) {
//       return response.data
//     }
//   } catch (ex) {
//     console.log(`exception: `, ex)
//   }
// }

export async function registerUser(
  firstName,
  lastName,
  email,
  phone,
  password
) {
  try {
    // create the required url
    const url = `${config.serverURL}/user/register`

    // create the request body
    const body = {
      firstName,
      lastName,
      email,
      phone,
      password,
    }

    // send the request and get the response from the server
    const response = await axios.post(url, body)

    if (response.status == 200) {
      // read the json body from response
      return response.data
    } else {
      // response is not success
      return null
    }
  } catch (ex) {
    console.log(`exception: `, ex)
  }
}

export async function loginUser(email, password) {
  try {
    // create url
    const url = `${config.serverURL}/user/login`

    // create a body
    const body = {
      email,
      password,
    }

    // call Post API
    const response = await axios.post(url, body)

    // check if response is OK
    if (response.status == 200) {
      // send the response body
      return response.data
    } else {
      // send null result
      return null
    }
  } catch (ex) {
    console.log(`exception: `, ex)
  }
}
