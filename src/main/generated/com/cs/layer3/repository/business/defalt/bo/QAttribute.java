package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAttribute is a Querydsl query type for Attribute
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAttribute extends EntityPathBase<Attribute> {

    private static final long serialVersionUID = 1849064032L;

    public static final QAttribute attribute = new QAttribute("attribute");

    public final QBusinessObject _super = new QBusinessObject(this);

    public final StringPath attributeName = createString("attributeName");

    public final StringPath defaultValue = createString("defaultValue");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final StringPath type = createString("type");

    public QAttribute(String variable) {
        super(Attribute.class, forVariable(variable));
    }

    public QAttribute(Path<? extends Attribute> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttribute(PathMetadata<?> metadata) {
        super(Attribute.class, metadata);
    }

}

