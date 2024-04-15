/* eslint-disable */
export const printANSI = () => {
  // console.clear()
  console.log('[RuoYi-Antdv] created()')
  console.log('POWERED BY RuoYi-Vue. Antd-Vue. Antd-Vue-Pro')
  // ASCII - ANSI Shadow
  let text = `
  8888888b.                Y88b   d88P d8b             d8888          888        888          
  888   Y88b                Y88b d88P  Y8P            d88888          888        888          
  888    888                 Y88o88P                 d88P888          888        888          
  888   d88P 888  888  .d88b. Y888P    888          d88P 888 88888b.  888888 .d88888 888  888 
  8888888P"  888  888 d88""88b 888     888         d88P  888 888 "88b 888   d88" 888 888  888 
  888 T88b   888  888 888  888 888     888 888888 d88P   888 888  888 888   888  888 Y88  88P 
  888  T88b  Y88b 888 Y88..88P 888     888       d8888888888 888  888 Y88b. Y88b 888  Y8bd8P  
  888   T88b  "Y88888  "Y88P"  888     888      d88P     888 888  888  "Y888 "Y88888   Y88P   
`
  console.log(`%c${text}`, 'color: #fc4d50')
  console.log('%cGithub: https://github.com/fuzui/RuoYi-Antdv', 'color: #fff; font-size: 14px; font-weight: 300; text-shadow:#000 1px 0 0,#000 0 1px 0,#000 -1px 0 0,#000 0 -1px 0;')
  console.log('%cGithub: https://gitee.com/fuzui/RuoYi-Antdv', 'color: #fff; font-size: 14px; font-weight: 300; text-shadow:#000 1px 0 0,#000 0 1px 0,#000 -1px 0 0,#000 0 -1px 0;')
}
