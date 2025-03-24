const srcBase: string = "https://cdn.akamai.steamstatic.com/apps/dota2/images/dota_react/heroes/";

export function Hero({ hero, handleClick, blocked, banned }) {

    return (
        <>
            <img className={banned ? 'banned-hero' : 'picked-hero'}
                key={hero.id}
                src={srcBase + hero.imageLink + '.png'}
                alt={hero.name}
                style={{
                    filter: blocked ? "grayscale(100%)" : "none",
                    cursor: blocked ? "not-allowed" : "pointer"

                }}
                onMouseDown={(e) => {
                    if (e.button == 1) {
                        e.preventDefault()
                    }
                    handleClick(hero.id, e.button);
                }}
                onContextMenu={(e) => e.preventDefault()}
            />
        </>
    )
}