
import { Hero } from "./Hero";

export function Recommendation({ r, heroes, handleHeroClick }: {
    r: string, heroes: {
        hero:
        {
            id: number,
            name: string,
            imageLink: string
        },
        winProb: number
    }[]
}) {

    return (
        <>
            <label style={{justifySelf:'center'}}>{r}</label>
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                {

                    heroes.map((item) => {
                        return (
                            <div key={item.hero.id} >
                                <b style={{
                                    position: 'absolute', right: 5, bottom: 5, zIndex: 2, fontFamily: 'a_LCDNova', color: 'white', fontSize: 21
                                    , textShadow: '2px 2px 0 black, -2px -2px 0 black, -2px 2px 0 black, 2px -2px 0 black'

                                }} >
                                    {(item.winProb).toFixed(2)}
                                </b>
                                <Hero key={item.hero.id} hero={item.hero} handleClick={handleHeroClick} blocked={false} banned={false}></Hero>
                                {/* <div style={{ position: 'relative', display: 'inline-block' }}>
                                    <b style={{
                                        position: 'absolute', right: 5, bottom: 5, zIndex: 2, fontFamily: 'a_LCDNova', color: 'white', fontSize: 21
                                        , textShadow: '2px 2px 0 black, -2px -2px 0 black, -2px 2px 0 black, 2px -2px 0 black'

                                    }} >
                                    </b>
                                    <img className="hero-image"
                                        src={"https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/" + item.hero.imageLink + ".png"}>
                                    </img>
                                </div> */}
                                {/* {hero.hero.name} (Winscore: {(hero.winProb).toFixed(2)}) */}
                            </div>);
                    })}
            </div>
        </>
    )
}


//{(hero.winProb).toFixed(2)}