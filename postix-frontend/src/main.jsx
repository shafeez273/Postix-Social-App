import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import PostixFrontend from './PostixFrontend.jsx'

createRoot(document.body).render(
  <StrictMode>
    <PostixFrontend/>
  </StrictMode>,
)
