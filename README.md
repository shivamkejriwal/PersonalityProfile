PersonalityProfile
==================

Creates a personality profile from facebook likes

The goal of this project is to determine the personality of an individual via the things he likes. I have made an assumption here that - there does exist a correlation between an individual’s personality and the things he likes. The model being used to represent a personality is based on the ‘Big 5 Personality Traits’ in various degrees from 0 to 100.
To gauge the degree of a person’s inventiveness, I have used the semantic similarity between things he likes and ‘inventiveness’. Semantic similarity would be the similarity between the literal meanings we associate to each thing. For example, if someone likes “Singing” then we analyze the semantic similarity of “singing” and “inventive.” However, I believe we associate more meaning to words then just its literal meaning. Therefore, each word being tested is expanded to a set of all related things. For example, if we were to analyze the semantic similarity of “Lady Gaga” and “ inventive” then Lady Gaga would be expanded to a set containing “singer”, ”artist”, ”concert”, ”music” etc. Each element in this new set would be analyzed against “inventive” and their total semantic similarity would be averaged. This process is applied to each entity liked by the person and the sum total is analyzed to determine each of his “Big 5 Personality Traits” on a scale of 0 to 100.




This project is still in the works and i'm in the process of finding ways to implemnt some of my thoretical assumptions. For example, exapansion of "Lady Gaga" to a set containing “singer”, ”artist”, ”concert”, ”music” etc contaions more noise then acceptable. Untill this gets resolved the acuraccy of my predictions is still low.
