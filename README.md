# Network Android Library

## Installation

Ajouter la librairie à votre projet en ajoutant la ligne suivante dans votre fichier
`build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.Fredbellano:network-android-library:1.0.0")
}
```

## Utilisation
Pour pouvoir faire des requêtes HTTP, il faut utiliser le client dans le fichier `RemoteDataSource.kt`:

```kotlin
RemoteDatSource.client
```

### Objet Result

Le client retourne un objet de type `Result<T>` pour chaques types de requetes.
Ce type permet de savoir si la requête a été un succès ou un échec.
Pour accéder à la valeur de la requête, il faut utiliser la méthode `getOrNull` de l'objet `Result<T>`:

```kotlin
val result: Result<T> = RemoteDataSource.client.get("{URL}")
val value: T = result.getOrNull() // or getOrThrow() pour obtenir la valeur ou lancer une exception
val exception: Throwable? = result.exceptionOrNull()
```

### Methode GET

Pour faire une requête GET, il faut utiliser la méthode `get` du client:

```kotlin
val result: Result<MyDataClass> = RemoteDataSource.client.get("{URL}")

// Avec des paramètres et headers
val result: Result<MyDataClass> =
    RemoteDataSource.client.get("{URL}",
        params = mapOf("param1" to "value1", "param2" to "value2"), 
        headers = mapOf("header1" to "value1", "header2" to "value2")
    )
```

### Methode POST

Pour faire une requête POST, il faut utiliser la méthode `post` du client:

```kotlin
val result: Result<MyDataClass> = RemoteDataSource.client.post("{URL}", body = MyDataClass())
```

### Methode PUT

Pour faire une requête PUT, il faut utiliser la méthode `put` du client:

```kotlin
val result: Result<MyDataClass> = RemoteDataSource.client.put("{URL}", body = MyDataClass())
```

### Methode DELETE

Pour faire une requête DELETE, il faut utiliser la méthode `delete` du client:

```kotlin
val result: Result<MyDataClass> = RemoteDataSource.client.delete("{URL}")
```


### Exemple d'utilisation dans un ViewModel

```kotlin
class ExampleViewModel : ViewModel() {
    fun fetchPosts() {
        // On utilise l'objet viewModelScope pour lancer une coroutine et faire notre appel réseau
        viewModelScope.launch {
            val result: Result<List<Post>> = RemoteDataSource.client.get(
                "https://jsonplaceholder.typicode.com/posts",
                queryParams = mapOf("userId" to "1"),
                headers = mapOf("Authorization" to "Bearer token")
            )
            
            // Vérifier si la requête a réussi
            when {
                result.isSuccess -> {
                    val posts = result.getOrThrow()
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("ExampleViewModel", "Erreur: ${exception?.message}")
                }
            }
        }
    }
}
```